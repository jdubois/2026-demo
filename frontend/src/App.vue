<script setup>
import { RouterView } from 'vue-router'
</script>

<template>
  <div class="app-shell">
    <header class="enterprise-header">
      <nav class="topbar" aria-label="Navigation principale">
        <a class="brand" href="#/">
          <span class="brand-mark" aria-hidden="true">TM</span>
          <span>
            <strong>Gestionnaire de tickets</strong>
            <small>Collecte open source</small>
          </span>
        </a>

        <div class="topbar-actions">
          <span class="environment-badge">
            <i class="bi bi-shield-check"></i>
            Prêt pour la production
          </span>
          <button class="btn btn-outline-secondary btn-sm" type="button">
            <i class="bi bi-bell"></i>
            Alertes
          </button>
        </div>
      </nav>

      <div class="page-heading">
        <div>
          <p class="eyebrow">Opérations du portefeuille Java</p>
          <h1>Pipeline de contributions</h1>
          <p class="hero-copy">
            Centralisez les issues candidates, suivez l'état d'exécution et gardez le travail des
            contributeurs aligné avec les priorités du projet.
          </p>
        </div>
        <div class="hero-badge">
          <i class="bi bi-graph-up-arrow"></i>
          <span>Tableau de bord exécutif</span>
        </div>
      </div>
    </header>

    <RouterView />
  </div>
</template>
