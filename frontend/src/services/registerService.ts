import type { IUser } from "../types/IUser";

const BASE_URL = "http://localhost:8080/api/auth/register";


export const registerService = {

    async register(firstName: string, lastName: string, email: string, password: string, phone: string) {
        const response = await fetch(BASE_URL, {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ firstName, lastName, email, phone, password })
        })
        if (!response.ok) {
            const errorData = await response.json();

            throw new Error(errorData.message || "Ocurrio un error en el servidor.")
        }
        return await response.json() as IUser;
    }
} 