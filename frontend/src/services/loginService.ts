import type { IUser } from "../types/IUser";

const BASE_URL = "http://localhost:8080/api/auth/login";

export const loginService = {

    async login(email: string, password: string) {
        const response = await fetch(BASE_URL, {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        })
        if (!response.ok) throw new Error("Failed to login.")
        return await response.json() as IUser;
    }
}