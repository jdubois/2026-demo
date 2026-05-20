<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { statusLabels, statusOptions, useTicketsStore } from '@/stores/tickets'

const ticketsStore = useTicketsStore()
const { activeTickets, error, loading, openTickets, tickets, totalTickets } =
  storeToRefs(ticketsStore)

const editingId = ref(null)
const form = reactive({
  title: '',
  repository: '',
  link: '',
  status: 'OPEN',
})

const sortedTickets = computed(() =>
  [...tickets.value].sort((first, second) => first.repository.localeCompare(second.repository)),
)

const completedTickets = computed(
  () => tickets.value.filter((ticket) => ticket.status === 'DONE').length,
)

onMounted(() => {
  ticketsStore.loadTickets()
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
}

function editTicket(ticket) {
  editingId.value = ticket.id
  form.title = ticket.title
  form.repository = ticket.repository
  form.link = ticket.link
  form.status = ticket.status
}

async function submitTicket() {
  await ticketsStore.saveTicket({
    id: editingId.value,
    title: form.title,
    repository: form.repository,
    link: form.link,
    status: form.status,
  })
  resetForm()
}
</script>

<template>
  <main class="ticket-board">
    <section class="metrics-grid">
      <article class="metric-card glass-card">
        <div class="metric-icon"><i class="bi bi-collection"></i></div>
        <div>
          <span>Total tickets</span>
          <strong>{{ totalTickets }}</strong>
        </div>
      </article>
      <article class="metric-card glass-card">
        <div class="metric-icon"><i class="bi bi-inbox"></i></div>
        <div>
          <span>Open opportunities</span>
          <strong>{{ openTickets }}</strong>
        </div>
      </article>
      <article class="metric-card glass-card">
        <div class="metric-icon"><i class="bi bi-kanban"></i></div>
        <div>
          <span>In progress</span>
          <strong>{{ activeTickets }}</strong>
        </div>
      </article>
      <article class="metric-card glass-card">
        <div class="metric-icon"><i class="bi bi-check2-square"></i></div>
        <div>
          <span>Completed</span>
          <strong>{{ completedTickets }}</strong>
        </div>
      </article>
    </section>

    <section class="content-grid">
      <form class="ticket-form glass-card" @submit.prevent="submitTicket">
        <div class="section-heading">
          <p>{{ editingId ? 'Change request' : 'Intake form' }}</p>
          <h2>{{ editingId ? 'Edit ticket record' : 'Register ticket' }}</h2>
        </div>

        <label class="form-label">
          Title
          <input v-model.trim="form.title" class="form-control" maxlength="200" required />
        </label>

        <label class="form-label">
          GitHub repository
          <input
            v-model.trim="form.repository"
            class="form-control"
            maxlength="120"
            placeholder="owner/repository"
            required
          />
        </label>

        <label class="form-label">
          GitHub issue link
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
          Status
          <select v-model="form.status" class="form-select">
            <option v-for="status in statusOptions" :key="status" :value="status">
              {{ statusLabels[status] }}
            </option>
          </select>
        </label>

        <div class="form-actions">
          <button class="btn btn-primary btn-lg" type="submit">
            <i class="bi bi-check2-circle"></i>
            {{ editingId ? 'Save changes' : 'Add ticket' }}
          </button>
          <button v-if="editingId" class="btn btn-light btn-lg" type="button" @click="resetForm">
            Cancel
          </button>
        </div>
      </form>

      <section class="ticket-list glass-card">
        <div class="list-toolbar">
          <div class="section-heading">
            <p>GitHub tickets</p>
            <h2>Contribution queue</h2>
          </div>
          <span class="record-count">{{ sortedTickets.length }} records</span>
        </div>

        <div v-if="error" class="alert alert-danger" role="alert">{{ error }}</div>
        <div v-if="loading" class="loading-state">
          <span class="spinner-border spinner-border-sm" aria-hidden="true"></span>
          Loading tickets...
        </div>

        <div class="ticket-table-header" aria-hidden="true">
          <span>Repository and issue</span>
          <span>Status</span>
          <span>Actions</span>
        </div>

        <article v-for="ticket in sortedTickets" :key="ticket.id" class="ticket-card">
          <div class="ticket-main">
            <span class="repo-name"><i class="bi bi-github"></i>{{ ticket.repository }}</span>
            <h3>{{ ticket.title }}</h3>
            <a :href="ticket.link" target="_blank" rel="noreferrer">
              View GitHub issue
              <i class="bi bi-box-arrow-up-right"></i>
            </a>
          </div>

          <div class="ticket-status">
            <span class="status-pill" :class="statusClass(ticket.status)">
              {{ statusLabels[ticket.status] }}
            </span>
          </div>

          <div class="ticket-actions">
            <button
              class="btn btn-outline-primary btn-sm"
              type="button"
              @click="editTicket(ticket)"
            >
              Edit
            </button>
            <button
              class="btn btn-outline-danger btn-sm"
              type="button"
              @click="ticketsStore.removeTicket(ticket.id)"
            >
              Remove
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
  gap: 1.25rem;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 0.9rem;
}

.metric-card {
  display: flex;
  align-items: center;
  gap: 0.9rem;
  padding: 1rem;
}

.metric-icon {
  display: grid;
  flex: 0 0 auto;
  width: 2.5rem;
  height: 2.5rem;
  place-items: center;
  color: #1d4ed8;
  background: #eff6ff;
  border: 1px solid #dbeafe;
  border-radius: 0.75rem;
  font-size: 1.05rem;
}

.metric-card span {
  color: #64748b;
  font-size: 0.72rem;
  font-weight: 800;
  letter-spacing: 0.07em;
  text-transform: uppercase;
}

.metric-card strong {
  display: block;
  margin-top: 0.15rem;
  color: #0f172a;
  font-size: 1.85rem;
  font-weight: 900;
  line-height: 1;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(280px, 360px) minmax(0, 1fr);
  gap: 1.25rem;
  align-items: start;
}

.ticket-form,
.ticket-list {
  padding: 1.25rem;
}

.section-heading {
  margin-bottom: 1.1rem;
}

.section-heading p {
  margin: 0 0 0.2rem;
  color: #2563eb;
  font-size: 0.72rem;
  font-weight: 900;
  letter-spacing: 0.12em;
  text-transform: uppercase;
}

.section-heading h2 {
  margin: 0;
  color: #0f172a;
  font-size: 1.18rem;
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
  color: #334155;
  font-size: 0.9rem;
  font-weight: 800;
}

.form-control,
.form-select {
  color: #0f172a;
  background-color: #f8fafc;
  border-color: #cbd5e1;
  border-radius: 0.55rem;
  padding: 0.72rem 0.8rem;
}

.form-control:focus,
.form-select:focus {
  background-color: #ffffff;
  border-color: #2563eb;
  box-shadow: 0 0 0 0.2rem rgba(37, 99, 235, 0.12);
}

.form-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.7rem;
  margin-top: 1.1rem;
}

.btn {
  border-radius: 0.55rem;
  font-weight: 800;
}

.btn-primary {
  background: #1d4ed8;
  border-color: #1d4ed8;
  box-shadow: 0 10px 18px rgba(29, 78, 216, 0.18);
}

.loading-state {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  color: #475569;
  font-weight: 800;
}

.list-toolbar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1rem;
  border-bottom: 1px solid #e2e8f0;
}

.record-count {
  padding: 0.35rem 0.55rem;
  color: #475569;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 0.45rem;
  font-size: 0.78rem;
  font-weight: 800;
  white-space: nowrap;
}

.ticket-table-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 150px 150px;
  gap: 1rem;
  padding: 0.75rem 0.9rem;
  color: #64748b;
  background: #f8fafc;
  border-bottom: 1px solid #e2e8f0;
  font-size: 0.72rem;
  font-weight: 900;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.ticket-card {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 150px 150px;
  align-items: center;
  gap: 1rem;
  padding: 1rem 0.9rem;
  background: #ffffff;
  border-bottom: 1px solid #e2e8f0;
}

.ticket-card:last-child {
  border-bottom: 0;
}

.repo-name {
  display: inline-flex;
  align-items: center;
  gap: 0.45rem;
  color: #475569;
  font-size: 0.82rem;
  font-weight: 900;
}

.ticket-main h3 {
  margin: 0.25rem 0 0.45rem;
  color: #0f172a;
  font-size: 1.03rem;
  font-weight: 900;
}

.ticket-main a {
  color: #2563eb;
  font-size: 0.9rem;
  font-weight: 800;
  text-decoration: none;
}

.ticket-status {
  display: flex;
  align-items: center;
}

.ticket-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 0.5rem;
}

@media (max-width: 980px) {
  .metrics-grid,
  .content-grid {
    grid-template-columns: 1fr 1fr;
  }

  .ticket-table-header {
    display: none;
  }

  .ticket-card {
    grid-template-columns: 1fr;
    border: 1px solid #e2e8f0;
    border-radius: 0.85rem;
    margin-top: 0.75rem;
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
    gap: 0.8rem;
  }

  .ticket-actions {
    justify-content: flex-start;
    flex-wrap: wrap;
  }
}
</style>
