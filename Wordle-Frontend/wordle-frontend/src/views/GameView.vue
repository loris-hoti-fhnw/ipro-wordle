<template>
  <div>
    <GameBoard :rows="rows" />

    <div  v-if="!gameOver" class="mt-3 d-flex justify-content-center">
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
      <button class="btn btn-success ms-2" @click="submitGuess">
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

    <div v-if="message" class="alert alert-info mt-3 text-center">
      {{ message }}
    </div>

    <div v-if="gameOver" class="text-center mt-3">
      <button class="btn btn-primary" @click="startNewGame">
        Neues Spiel starten
      </button>
    </div>


  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
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

const activeKey = ref<string>('')


watch(guess, (newValue, oldValue) => {
  if (newValue.length > oldValue.length) {
    activeKey.value = newValue[newValue.length - 1]?.toUpperCase() ?? ''

    setTimeout(() => {
      activeKey.value = ''
    }, 1000)
  }
})


async function submitGuess() {
  if (guess.value.length !== 5 || gameOver.value) return

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

  guess.value = ''
  guessInput.value?.focus()
}

async function startNewGame() {
  const response = await api.get<{ gameId: number }>('/game/start')

  rows.value = []
  message.value = ''
  guess.value = ''
  gameOver.value = false
  guessInput.value?.focus()

  gameId.value = response.data.gameId.toString()

  router.push(`/game/${gameId.value}`)
}


</script>

<style scoped>
.guess-tile {
  width: 50px;
  height: 50px;
  margin: 4px;
  border: 2px solid #888;

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
.keyboard {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  max-width: 500px;
  margin: auto;
}

.key {
  width: 40px;
  height: 45px;
  margin: 3px;
  background-color: #ddd;
  border-radius: 4px;

  display: flex;
  align-items: center;
  justify-content: center;

  font-weight: bold;
}

.active-key {
  background-color: #0d6efd;
  color: white;
}

</style>