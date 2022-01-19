export type LoginCredentials = {
  email: string,
  password: string
}

export type ApiToken = {
  token: string
}

export type LoggedUser = {
  email: string,
  groups: string[]
}
