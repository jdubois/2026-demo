<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <div class="app-shell">
    <div class="hero">
      <div>
        <p class="eyebrow">Assistant Java open source</p>
        <h1>Radar de bons tickets</h1>
        <p class="hero-copy">
          Sélectionnez des issues GitHub accessibles, suivez votre progression et aidez les projets
          Java à recevoir d'excellentes premières contributions.
        </p>
      </div>
      <div class="hero-badge">
        <i class="bi bi-stars"></i>
        <span>Alimenté par des découvertes MCP</span>
      </div>
    </div>

    <RouterView />
  </div>
</template>
