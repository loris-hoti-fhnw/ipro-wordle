<template>
  <div class="game-layout">

    <!-- Statistik -->
    <div class="side-panel">
      <div class="statistics-box">
        <h4>Statistik</h4>

        <div class="stat-item">
          <span>Spiele</span>
          <strong>{{ statistics.gamesPlayed }}</strong>
        </div>

        <div class="stat-item">
          <span>Versuche</span>
          <strong>{{ statistics.totalAttempts }}</strong>
        </div>

        <div class="stat-item">
          <span>Ø Versuche</span>
          <strong>{{ statistics.averageAttempts.toFixed(1) }}</strong>
        </div>
      </div>
    </div>


    <!-- Wordle -->
    <div class="game-center" @click="focusInput">

      <div class="game-info mb-4">
        <span>6 Versuche pro Spiel</span>
        <span>Nur Englische Wörter</span>
      </div>

      <GameBoard :rows="rows" />

      <div v-if="!gameOver" class="guess-area">

        <div class="d-flex justify-content-center">
          <div
              v-for="i in 5"
              :key="i"
              class="guess-tile"
          >
            {{ guess[i - 1] || '' }}
          </div>
        </div>

        <input
            ref="guessInput"
            v-model="guess"
            maxlength="5"
            class="hidden-input"
            autofocus
        />

        <button class="btn btn-success mt-3" @click="submitGuess">
          OK
        </button>
      </div>


      <div class="keyboard mt-4">
        <div
            v-for="key in keyboard"
            :key="key"
            class="key"
            :class="{ 'active-key': activeKey === key }"
        >
          {{ key }}
        </div>
      </div>


      <div v-if="message" class="alert alert-info mt-4 text-center">
        {{ message }}
      </div>


      <div v-if="gameOver" class="text-center mt-3">
        <button class="btn btn-primary" @click="startNewGame">
          Neues Spiel starten
        </button>
      </div>

    </div>


    <!-- Hints -->
    <div class="side-panel">
      <div class="hint-box">

        <h4>Hints</h4>

        <p class="text-muted">
          Du kannst pro Spiel 2 Hinweise verwenden.
        </p>

        <button
            class="btn btn-warning w-100"
            @click="getHint"
            :disabled="hintsUsed >= 2 || gameOver"
        >
          Hint holen
        </button>

        <div class="mt-3">
          Verbleibend: {{ 2 - hintsUsed }}
        </div>

        <div v-if="hints.length > 0" class="mt-4">
          <strong>Hinweise:</strong>

          <div
              v-for="(hint, index) in hints"
              :key="index"
              class="hint-letter"
          >
            {{ hint }}
          </div>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter} from 'vue-router'
import api from '../services/api'
import GameBoard from '../components/GameBoard.vue'


interface GuessResponse {
  feedback: string
  completed: boolean
  success: boolean
  solutionWord: string
}

interface Row {
  word: string
  feedback: string
}

interface StatisticsResponse {
  gamesPlayed: number
  totalAttempts: number
  averageAttempts: number
}



const route = useRoute()
const gameId = ref<string>(route.params.id as string)
const router = useRouter()

const guess = ref<string>('')
const rows = ref<Row[]>([])
const message = ref<string>('')
const gameOver = ref(false)
const guessInput = ref<HTMLInputElement | null>(null)

const keyboard = [
  'Q','W','E','R','T','Z','U','I','O','P',
  'A','S','D','F','G','H','J','K','L',
  'Y','X','C','V','B','N','M'
]

const hints = ref<string[]>([])
const hintsUsed = ref(0)

interface StatisticsResponse {
  gamesPlayed: number
  totalAttempts: number
  averageAttempts: number
}

const activeKey = ref<string>('')

const statistics = ref<StatisticsResponse>({
  gamesPlayed: 0,
  totalAttempts: 0,
  averageAttempts: 0
})


watch(guess, (newValue, oldValue) => {
  if (newValue.length > oldValue.length) {
    activeKey.value = newValue[newValue.length - 1]?.toUpperCase() ?? ''

    setTimeout(() => {
      activeKey.value = ''
    }, 1000)
  }
})

function focusInput() {
  if (!gameOver.value) {
    guessInput.value?.focus()
  }
}

async function getHint() {
  if (hintsUsed.value >= 2 || gameOver.value) return

  const response = await api.get<string>(`/game/${gameId.value}/hint`)

  hints.value.push(response.data)
  hintsUsed.value++
}

async function loadStatistics() {
  const response = await api.get<StatisticsResponse>('/game/stats')
  statistics.value = response.data
}

async function submitGuess() {
  if (guess.value.length !== 5 || gameOver.value) return

  try {
    const response = await api.post<GuessResponse>('/game/guess', {
      gameId: Number(gameId.value),
      guess: guess.value
    })

    rows.value.push({
      word: guess.value.toUpperCase(),
      feedback: response.data.feedback
    })

    if (response.data.success) {
      message.value = `Gewonnen! Das Wort war: ${response.data.solutionWord}`
      gameOver.value = true
    } else if (response.data.completed) {
      message.value = `Spiel verloren. Das richtige Wort war: ${response.data.solutionWord}`
      gameOver.value = true
    }

    if (response.data.completed) {
      await loadStatistics()
    }

    guess.value = ''
    guessInput.value?.focus()

  } catch {
    message.value = 'Dieses Wort ist nicht in der Wortliste.'
    guessInput.value?.focus()
  }
}

async function startNewGame() {
  const response = await api.get<{ gameId: number }>('/game/start')

  rows.value = []
  message.value = ''
  guess.value = ''
  gameOver.value = false
  guessInput.value?.focus()
  hints.value = []
  hintsUsed.value = 0

  gameId.value = response.data.gameId.toString()

  router.push(`/game/${gameId.value}`)
}

onMounted(() => {
  loadStatistics()
})

</script>

<style scoped>

.game-info {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.game-info span {
  background-color: #f1f3f5;
  border: 1px solid #ddd;
  border-radius: 20px;
  padding: 8px 15px;
  font-size: 14px;
  font-weight: 500;
}

.game-layout {
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  gap: 30px;

  width: 100%;
  max-width: 1400px;
  margin: 0 auto;

  align-items: start;
}


/* LINKE UND RECHTE SEITE */

.side-panel {
  padding: 10px;
}

.statistics-box,
.right-box {
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 20px;
}

.statistics-box h4,
.right-box h4 {
  text-align: center;
  margin-bottom: 20px;
}


/* STATISTIK */

.stat-item {
  display: flex;
  justify-content: space-between;
  align-items: center;

  padding: 15px 5px;
  border-bottom: 1px solid #ddd;
}

.stat-item:last-child {
  border-bottom: none;
}

.stat-item span {
  font-size: 16px;
}

.stat-item strong {
  font-size: 24px;
}


/* MITTE */

.game-center {
  text-align: center;
  min-width: 0;
}

.guess-area {
  margin-top: 20px;
}


/* EINGABEKACHELN */

.guess-tile {
  width: 50px;
  height: 50px;
  margin: 4px;

  border: 2px solid #888;
  border-radius: 5px;

  display: flex;
  align-items: center;
  justify-content: center;

  font-size: 24px;
  font-weight: bold;
  text-transform: uppercase;
}

.hidden-input {
  position: absolute;
  opacity: 0;
}


/* TASTATUR */

.keyboard {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;

  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
}

.key {
  width: 40px;
  height: 45px;
  margin: 3px;

  background-color: #ddd;
  border-radius: 5px;

  display: flex;
  align-items: center;
  justify-content: center;

  font-weight: bold;
}

.active-key {
  background-color: #0d6efd;
  color: white;
}

/* HINTS */

.hint-box {
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
}

.hint-box h4 {
  margin-bottom: 20px;
}

.hint-letter {
  width: 50px;
  height: 50px;

  margin: 10px auto;

  border: 2px solid #ffc107;
  border-radius: 5px;

  display: flex;
  align-items: center;
  justify-content: center;

  font-size: 24px;
  font-weight: bold;
}


/* KLEINERE BILDSCHIRME */

@media (max-width: 900px) {

  .game-layout {
    grid-template-columns: 1fr;
  }

  .side-panel {
    max-width: 500px;
    width: 100%;
    margin: auto;
  }

}
</style>