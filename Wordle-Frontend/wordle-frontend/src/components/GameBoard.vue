<template>
  <div class="d-flex justify-content-center">
    <div>
      <div v-for="(row, rowIndex) in rows" :key="rowIndex" class="d-flex">
        <div
            v-for="(char, i) in row.word"
            :key="i"
            class="tile"
            :class="color(row.feedback[i] ?? '')"
        >
          {{ char }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
interface Row {
  word: string
  feedback: string
}

defineProps<{
  rows: Row[]
}>()

function color(c: string): string {
  if (c === 'G') return 'bg-success'
  if (c === 'Y') return 'bg-warning'
  return 'bg-secondary'
}
</script>

<style scoped>
.tile {
  width: 50px;
  height: 50px;
  margin: 4px;
  color: white;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  text-transform: uppercase;
}
</style>
