export type LoginCredentials = {
  email: string,
  password: string
}

export type LoginToken = {
  apiToken: string,
  expiresAtSeconds: number
}
