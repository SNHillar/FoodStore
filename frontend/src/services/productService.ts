import type { Product } from "../types/product";

const API_URL = 'http://localhost:8080/api/products';

export const productService = {

    async getAll(): Promise<Product[]> {
        const response = await fetch(API_URL);
        if(!response.ok) throw new Error("Failed to fetch products.")
        return await response.json() as Product[];    
    },

    async getById(id: number): Promise<Product> {
    const response = await fetch(`${API_URL}/${id}`, {
      method: 'GET'
    });
    if (!response.ok) throw new Error('Failed to get product');
    return await response.json() as Product;
    },


    async update(id: number, name: string, price: number, description: string, stock: number, image: string, categoryId: number ){
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({id, name, price, description, stock, image, categoryId})
        })
        if(response.status == 403 || response.status == 401){
            throw new Error("Unauthorized access.")
        }
        if(!response.ok) throw new Error("Failed to update product.")
        return await response.json() as Product;    
    },

    async create(name: string, price: number, description: string, stock: number, image: string, categoryId: number ){
        const response = await fetch(API_URL, {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({name, price, description, stock, image, categoryId})
        })
        if(response.status === 403 || response.status === 401){
            throw new Error("Unauthorized access.")
        }
        if(!response.ok) throw new Error("Failed to create product.")
        return await response.json() as Product;    
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