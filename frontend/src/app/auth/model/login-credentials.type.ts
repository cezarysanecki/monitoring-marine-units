export type LoginCredentials = {
  email: string,
  password: string
}

export type ApiToken = {
  token: string,
  refreshToken: string
}

export type LoggedUser = {
  email: string,
  groups: string[],
  tokens: ApiToken
}
