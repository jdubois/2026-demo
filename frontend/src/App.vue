<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <div class="app-shell">
    <div class="hero">
      <div>
        <p class="eyebrow">Java Open Source helper</p>
        <h1>Good Ticket Radar</h1>
        <p class="hero-copy">
          Curate approachable GitHub issues, track your progress, and help Java projects get
          excellent first contributions.
        </p>
      </div>
      <div class="hero-badge">
        <i class="bi bi-stars"></i>
        <span>Seeded with MCP discoveries</span>
      </div>
    </div>

    <RouterView />
  </div>
</template>
