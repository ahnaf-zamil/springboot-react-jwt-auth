export interface AuthResponse {
    accessToken: string;
    refreshToken: string;
}

export interface User {
    id: string;
    name: string;
    email: string;
}