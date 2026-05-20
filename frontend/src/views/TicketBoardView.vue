<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { statusLabels, statusOptions, useTicketsStore } from '@/stores/tickets'

const ticketsStore = useTicketsStore()
const { activeTickets, error, loading, openTickets, tickets, totalTickets, users } =
  storeToRefs(ticketsStore)

const editingId = ref(null)
const form = reactive({
  title: '',
  repository: '',
  link: '',
  status: 'OPEN',
  assigneeId: '',
})

const sortedTickets = computed(() =>
  [...tickets.value].sort((first, second) => first.repository.localeCompare(second.repository)),
)

const completedTickets = computed(() => tickets.value.filter((ticket) => ticket.status === 'DONE').length)

onMounted(async () => {
  await ticketsStore.loadTicketBoard()
  if (!form.assigneeId) {
    form.assigneeId = users.value[0]?.id ?? ''
  }
})

function statusClass(status) {
  return `status-${status.toLowerCase().replaceAll('_', '-')}`
}

function resetForm() {
  editingId.value = null
  form.title = ''
  form.repository = ''
  form.link = ''
  form.status = 'OPEN'
  form.assigneeId = users.value[0]?.id ?? ''
}

function editTicket(ticket) {
  editingId.value = ticket.id
  form.title = ticket.title
  form.repository = ticket.repository
  form.link = ticket.link
  form.status = ticket.status
  form.assigneeId = ticket.assignee?.id ?? ''
}

async function submitTicket() {
  await ticketsStore.saveTicket({
    id: editingId.value,
    title: form.title,
    repository: form.repository,
    link: form.link,
    status: form.status,
    assigneeId: Number(form.assigneeId),
  })
  resetForm()
}
</script>

<template>
  <main class="ticket-board">
    <section class="metrics-grid">
      <article class="metric-card glass-card">
        <span>Total des tickets</span>
        <strong>{{ totalTickets }}</strong>
      </article>
      <article class="metric-card glass-card">
        <span>Opportunités ouvertes</span>
        <strong>{{ openTickets }}</strong>
      </article>
      <article class="metric-card glass-card">
        <span>En cours</span>
        <strong>{{ activeTickets }}</strong>
      </article>
      <article class="metric-card glass-card">
        <span>Terminés</span>
        <strong>{{ completedTickets }}</strong>
      </article>
    </section>

    <section class="content-grid">
      <form class="ticket-form glass-card" @submit.prevent="submitTicket">
        <div class="section-heading">
          <p>{{ editingId ? 'Mettre à jour une cible de contribution' : 'Ajouter une cible de contribution' }}</p>
          <h2>{{ editingId ? 'Modifier le ticket' : 'Nouveau ticket' }}</h2>
        </div>

        <label class="form-label">
          Titre
          <input v-model.trim="form.title" class="form-control" maxlength="200" required />
        </label>

        <label class="form-label">
          Dépôt GitHub
          <input
            v-model.trim="form.repository"
            class="form-control"
            maxlength="120"
            placeholder="owner/repository"
            required
          />
        </label>

        <label class="form-label">
          Lien de l'issue GitHub
          <input
            v-model.trim="form.link"
            class="form-control"
            maxlength="300"
            pattern="https://github.com/.+/.+/issues/[0-9]+"
            placeholder="https://github.com/owner/repository/issues/123"
            required
          />
        </label>

        <label class="form-label">
          Statut
          <select v-model="form.status" class="form-select">
            <option v-for="status in statusOptions" :key="status" :value="status">
              {{ statusLabels[status] }}
            </option>
          </select>
        </label>

        <label class="form-label">
          Assigné
          <select v-model="form.assigneeId" class="form-select" required>
            <option disabled value="">Sélectionner un utilisateur</option>
            <option v-for="user in users" :key="user.id" :value="user.id">
              {{ user.username }}
            </option>
          </select>
        </label>

        <div class="form-actions">
          <button class="btn btn-primary btn-lg" type="submit">
            <i class="bi bi-check2-circle"></i>
            {{ editingId ? 'Enregistrer les modifications' : 'Ajouter le ticket' }}
          </button>
          <button v-if="editingId" class="btn btn-light btn-lg" type="button" @click="resetForm">
            Annuler
          </button>
        </div>
      </form>

      <section class="ticket-list glass-card">
        <div class="section-heading">
          <p>Tickets GitHub</p>
          <h2>File de contributions</h2>
        </div>

        <div v-if="error" class="alert alert-danger" role="alert">{{ error }}</div>
        <div v-if="loading" class="loading-state">
          <span class="spinner-border spinner-border-sm" aria-hidden="true"></span>
          Chargement des tickets...
        </div>

        <article v-for="ticket in sortedTickets" :key="ticket.id" class="ticket-card">
          <div class="ticket-main">
            <span class="repo-name"><i class="bi bi-github"></i>{{ ticket.repository }}</span>
            <h3>{{ ticket.title }}</h3>
            <a :href="ticket.link" target="_blank" rel="noreferrer">
              Voir l'issue GitHub
              <i class="bi bi-box-arrow-up-right"></i>
            </a>
            <span class="assignee-name">
              <i class="bi bi-person-circle"></i>
              {{ ticket.assignee?.username ?? 'Non assigné' }}
            </span>
          </div>

          <div class="ticket-actions">
            <span class="status-pill" :class="statusClass(ticket.status)">
              {{ statusLabels[ticket.status] }}
            </span>
            <button class="btn btn-outline-primary btn-sm" type="button" @click="editTicket(ticket)">
              Modifier
            </button>
            <button
              class="btn btn-outline-danger btn-sm"
              type="button"
              @click="ticketsStore.removeTicket(ticket.id)"
            >
              Supprimer
            </button>
          </div>
        </article>
      </section>
    </section>
  </main>
</template>

<style scoped>
.ticket-board {
  display: grid;
  gap: 1.5rem;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 1rem;
}

.metric-card {
  padding: 1.2rem;
}

.metric-card span {
  color: #6b7588;
  font-size: 0.78rem;
  font-weight: 800;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.metric-card strong {
  display: block;
  margin-top: 0.3rem;
  color: #1d2b4f;
  font-size: 2.25rem;
  line-height: 1;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(280px, 390px) minmax(0, 1fr);
  gap: 1.5rem;
  align-items: start;
}

.ticket-form,
.ticket-list {
  padding: 1.4rem;
}

.section-heading {
  margin-bottom: 1rem;
}

.section-heading p {
  margin: 0 0 0.2rem;
  color: #6677a0;
  font-size: 0.78rem;
  font-weight: 900;
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

.section-heading h2 {
  margin: 0;
  color: #192647;
  font-size: 1.45rem;
  font-weight: 900;
}

.ticket-form {
  position: sticky;
  top: 1rem;
}

.form-label {
  display: grid;
  gap: 0.35rem;
  margin-bottom: 0.9rem;
  color: #2d3b5f;
  font-weight: 800;
}

.form-control,
.form-select {
  border-color: #dce5f5;
  border-radius: 0.9rem;
  padding: 0.75rem 0.85rem;
}

.form-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.7rem;
  margin-top: 1.1rem;
}

.btn {
  border-radius: 999px;
  font-weight: 800;
}

.btn-primary {
  background: linear-gradient(135deg, #635bff, #2cc59e);
  border: 0;
  box-shadow: 0 12px 24px rgba(99, 91, 255, 0.24);
}

.loading-state {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  color: #58647a;
  font-weight: 800;
}

.ticket-card {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem;
  margin-top: 0.9rem;
  background: #ffffff;
  border: 1px solid #e5ebf6;
  border-radius: 1.15rem;
  box-shadow: 0 12px 30px rgba(25, 39, 88, 0.08);
}

.repo-name {
  display: inline-flex;
  align-items: center;
  gap: 0.45rem;
  color: #5c6b88;
  font-size: 0.85rem;
  font-weight: 900;
}

.ticket-main h3 {
  margin: 0.25rem 0 0.45rem;
  color: #17233f;
  font-size: 1.03rem;
  font-weight: 900;
}

.ticket-main a {
  color: #4a58dc;
  font-weight: 800;
  text-decoration: none;
}

.assignee-name {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  margin-left: 0.8rem;
  color: #6677a0;
  font-size: 0.85rem;
  font-weight: 800;
}

.ticket-actions {
  display: flex;
  align-items: flex-end;
  flex-direction: column;
  gap: 0.45rem;
  min-width: 128px;
}

@media (max-width: 980px) {
  .metrics-grid,
  .content-grid {
    grid-template-columns: 1fr 1fr;
  }

  .ticket-form {
    position: static;
  }
}

@media (max-width: 720px) {
  .metrics-grid,
  .content-grid {
    grid-template-columns: 1fr;
  }

  .ticket-card {
    flex-direction: column;
  }

  .ticket-actions {
    align-items: flex-start;
    flex-direction: row;
    flex-wrap: wrap;
  }
}
</style>
