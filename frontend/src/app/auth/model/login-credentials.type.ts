export type LoginCredentials = {
  email: string,
  password: string
}

export type UserTokens = {
  apiToken: string,
  refreshToken: string
}

export type LoggedUser = {
  email: string,
  groups: string[],
  tokens: UserTokens
}
