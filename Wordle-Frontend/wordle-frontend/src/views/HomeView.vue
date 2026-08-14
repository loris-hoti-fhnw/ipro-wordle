<template>
  <div class="text-center">
    <button class="btn btn-primary btn-lg" @click="startGame">
      Neues Spiel starten
    </button>
  </div>
</template>

<script setup lang="ts">
import api from '../services/api'
import { useRouter } from 'vue-router'

interface StartGameResponse {
  gameId: number
}

const router = useRouter()

async function startGame() {
  const response = await api.get<StartGameResponse>('/game/start')
  router.push(`/game/${response.data.gameId}`)
}
</script>
