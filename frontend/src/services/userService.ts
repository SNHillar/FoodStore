import type { IUser } from "../types/IUser";

const API_URL = 'http://localhost:8080/api/users';

export const userService = {

    async getAll(): Promise<IUser[]> {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Failed to fetch users");
        return await response.json() as IUser[];
    },

    async getById(id: number): Promise<IUser> {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error("Failed to fetch user");
        return await response.json() as IUser;
    },

    async create(user: IUser): Promise<IUser> {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        });
        if (!response.ok) throw new Error("Failed to create user");
        return await response.json() as IUser;
    },

    async update(id: number, user: IUser): Promise<IUser> {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(user)
        });
        if (!response.ok) throw new Error("Failed to update user");
        return await response.json() as IUser;
    },

    async delete(id: number): Promise<void> {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "DELETE"
        });
        if (!response.ok) throw new Error("Failed to delete user");
    }
}