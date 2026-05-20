<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <div class="app-shell">
    <header class="enterprise-header">
      <nav class="topbar" aria-label="Primary navigation">
        <a class="brand" href="#/">
          <span class="brand-mark" aria-hidden="true">TM</span>
          <span>
            <strong>Ticket Manager</strong>
            <small>Open source intake</small>
          </span>
        </a>

        <div class="topbar-actions">
          <span class="environment-badge">
            <i class="bi bi-shield-check"></i>
            Production ready
          </span>
          <button class="btn btn-outline-secondary btn-sm" type="button">
            <i class="bi bi-bell"></i>
            Alerts
          </button>
        </div>
      </nav>

      <div class="page-heading">
        <div>
          <p class="eyebrow">Java portfolio operations</p>
          <h1>Contribution pipeline</h1>
          <p class="hero-copy">
            Centralize candidate issues, monitor execution status, and keep contributor work aligned
            with project priorities.
          </p>
        </div>
        <div class="hero-badge">
          <i class="bi bi-graph-up-arrow"></i>
          <span>Executive dashboard</span>
        </div>
      </div>
    </header>

    <RouterView />
  </div>
</template>
