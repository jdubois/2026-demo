import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import {
  createTicket,
  deleteTicket,
  fetchTickets,
  updateTicket,
} from '@/services/tickets'

export const statusOptions = ['OPEN', 'IN_PROGRESS', 'TO_REVIEW', 'DONE', 'ARCHIVED']

export const statusLabels = {
  OPEN: 'Ouvert',
  IN_PROGRESS: 'En cours',
  TO_REVIEW: 'À relire',
  DONE: 'Terminé',
  ARCHIVED: 'Archivé',
}

export const useTicketsStore = defineStore('tickets', () => {
  const tickets = ref([])
  const loading = ref(false)
  const error = ref('')

  const totalTickets = computed(() => tickets.value.length)
  const openTickets = computed(() => tickets.value.filter((ticket) => ticket.status === 'OPEN').length)
  const activeTickets = computed(
    () => tickets.value.filter((ticket) => ['IN_PROGRESS', 'TO_REVIEW'].includes(ticket.status)).length,
  )

  async function loadTickets() {
    loading.value = true
    error.value = ''
    try {
      tickets.value = await fetchTickets()
    } catch (requestError) {
      error.value = requestError.message
    } finally {
      loading.value = false
    }
  }

  async function saveTicket(ticket) {
    error.value = ''
    const savedTicket = ticket.id ? await updateTicket(ticket) : await createTicket(ticket)
    const index = tickets.value.findIndex((existingTicket) => existingTicket.id === savedTicket.id)
    if (index === -1) {
      tickets.value = [...tickets.value, savedTicket]
    } else {
      tickets.value[index] = savedTicket
    }
  }

  async function removeTicket(id) {
    error.value = ''
    await deleteTicket(id)
    tickets.value = tickets.value.filter((ticket) => ticket.id !== id)
  }

  return {
    activeTickets,
    error,
    loadTickets,
    loading,
    openTickets,
    removeTicket,
    saveTicket,
    tickets,
    totalTickets,
  }
})
