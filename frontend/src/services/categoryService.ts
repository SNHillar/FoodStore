import type { ICategory } from "../types/category";

const API_URL = 'http://localhost:8080/api/categories';

export const CategoriaService = {

  async getAll(): Promise<ICategory[]> {
    const response = await fetch(API_URL, {
      method: 'GET'
    });
    if (!response.ok) throw new Error('Failed to fetch categories');
    return await response.json() as ICategory[];
  },


  async create (name: string, description: string) {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, description })
    });

    if (response.status === 403 || response.status === 401) {
      throw new Error('Unauthorized access');
    }
    if (!response.ok) throw new Error('Failed to create category');
    return await response.json() as ICategory;
  },

  async update(id: number, name: string, description: string) {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, description })
    });
    if (response.status === 403 || response.status === 401) {
      throw new Error('Unauthorized access');
    }
    if (!response.ok) throw new Error('Failed to update category');
    return await response.json() as ICategory;
  },

  async delete(id: number) {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'DELETE'
    });
    if (response.status === 403 || response.status === 401) {
      throw new Error('Unauthorized access');
    }
    if (!response.ok) throw new Error('Failed to delete category');
  }

}