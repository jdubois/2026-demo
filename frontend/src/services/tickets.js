const jsonHeaders = {
  'Content-Type': 'application/json',
}

async function parseResponse(response) {
  if (response.status === 204) {
    return null
  }

  const payload = await response.json()
  if (!response.ok) {
    throw new Error(payload.detail ?? payload.message ?? 'The ticket request failed.')
  }
  return payload
}

export async function fetchTickets() {
  const response = await fetch('/api/tickets')
  return parseResponse(response)
}

export async function fetchUsers() {
  const response = await fetch('/api/users')
  return parseResponse(response)
}

export async function createTicket(ticket) {
  const response = await fetch('/api/tickets', {
    method: 'POST',
    headers: jsonHeaders,
    body: JSON.stringify(ticket),
  })
  return parseResponse(response)
}

export async function updateTicket(ticket) {
  const response = await fetch(`/api/tickets/${ticket.id}`, {
    method: 'PUT',
    headers: jsonHeaders,
    body: JSON.stringify(ticket),
  })
  return parseResponse(response)
}

export async function deleteTicket(id) {
  const response = await fetch(`/api/tickets/${id}`, {
    method: 'DELETE',
  })
  return parseResponse(response)
}
