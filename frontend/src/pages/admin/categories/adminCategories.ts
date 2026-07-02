import type { ICategory } from "../../../types/category";
import { CategoriaService } from "../../../services/categoryService";
import { ACTIONS_ICONS, TOAST_ICONS } from "../../../utils/icons";
import { showToast } from "../../../utils/toast";
import type { Product } from "../../../types/product";

const addBtn = document.getElementById('create-btn') as HTMLButtonElement;
const categoriesList = document.getElementById('categories-list') as HTMLDivElement;
const modalCategory = document.getElementById('categoryModal') as HTMLDivElement;
const categoryForm = document.getElementById("categoryForm") as HTMLFormElement;
const categoryNameInput = document.getElementById("categoryName") as HTMLInputElement;
const categoryDescriptionInput = document.getElementById("categoryDescription") as HTMLTextAreaElement;
const closeModalBtn = document.getElementById('closeModalBtn') as HTMLButtonElement;
const closeDeleteModalBtn = document.getElementById('closeDeleteModalBtn') as HTMLButtonElement;
const confirmDeleteCategoryBtn = document.getElementById('confirmDeleteCategoryBtn') as HTMLButtonElement;
const deleteCategoryModal = document.getElementById('deleteCategoryModal') as HTMLDivElement;

let categories: ICategory[] = [];
let editingCategoryId: number | null = null;

async function loadCategories() {
    try {
        categories = await CategoriaService.getAll();
        renderCategories(categories);
    } catch (error) {   
        console.error('Error al cargar categorías:', error);
        showToast('Error al cargar categorías', 'error');
        return;
    }
}

function renderCategories(categories: ICategory[]) {

    if (categories == null) return;
    categoriesList.innerHTML = '';
    categories.forEach(category => {
        const categoryElement = document.createElement('article');
        categoryElement.classList.add('management-card');

        const cardContent = document.createElement('div') as HTMLDivElement;
        cardContent.classList.add('management-card__content', 'management-card__content--categories')


        const categoryTitle = document.createElement('h3') as HTMLHeadingElement;
        categoryTitle.classList.add('category-card__title');
        categoryTitle.textContent = `${category.name}`;

        const categoryDescription = document.createElement('p') as HTMLParagraphElement;
        categoryDescription.classList.add('category-card__description');
        categoryDescription.textContent = `${category.description}`;

        const actionsBtns = document.createElement('div') as HTMLDivElement;
        actionsBtns.classList.add('management-card__actions')

        const editBtn = document.createElement('button') as HTMLButtonElement;
        editBtn.classList.add('btn-icon-action', 'btn-icon-action--edit');
        editBtn.dataset.id = `${category.id}`;
        editBtn.innerHTML = `${ACTIONS_ICONS['Edit']}<span>Editar</span>`;

        const deleteBtn = document.createElement('button') as HTMLButtonElement;
        deleteBtn.classList.add('btn-icon-action', 'btn-icon-action--delete');
        deleteBtn.dataset.id = `${category.id}`;
        deleteBtn.innerHTML = `${ACTIONS_ICONS['Delete']}<span>Eliminar</span>`;

        actionsBtns.append(editBtn, deleteBtn);
        cardContent.append(categoryTitle, categoryDescription, actionsBtns);
        categoryElement.append(cardContent);
        categoriesList.appendChild(categoryElement);
    });
}

addBtn.addEventListener('click', () => {
    editingCategoryId = null;
    categoryForm.reset();
    modalCategory.classList.add('modal-overlay--show');
    renderCategories(categories);
});

categoryForm?.addEventListener('submit', async (event: SubmitEvent) => {

    event.preventDefault();

    const nameValue = categoryNameInput.value.trim();
    const descriptionValue = categoryDescriptionInput.value.trim();

    try {
        if (!editingCategoryId) {
            await CategoriaService.create(nameValue, descriptionValue);
            showToast('Categoría creada con éxito.', 'success');
        } else {
            await CategoriaService.update(editingCategoryId, nameValue, descriptionValue);
            showToast('Categoría actualizada con éxito.', 'success');
        }
        categoryForm.reset();
        editingCategoryId = null;
        modalCategory.classList.remove('modal-overlay--show');
        const updateCategories = await CategoriaService.getAll();
        renderCategories(updateCategories);
    } catch (error) {
        console.error("Error al crear la categoría desde el modal:", error);
        showToast("Hubo un error al guardar la categoría. Revisá la consola.", 'error');
        return;
    }
})

closeModalBtn?.addEventListener('click', async () => {
    // 1. Cerramos el modal sacándole la clase
    modalCategory.classList.remove('modal-overlay--show');
    categoryForm.reset();
    if (typeof editingCategoryId !== 'undefined') {
        editingCategoryId = null;
    }
    const updateCategories = await CategoriaService.getAll();
    renderCategories(updateCategories);
    return;
});


document.addEventListener('DOMContentLoaded', () => {
    loadCategories();

    categoriesList.addEventListener('click', (event) => {
            const target = event.target as HTMLElement;
            const isDelete = target.closest('.btn-icon-action--delete') as HTMLButtonElement;
            const isEdit = target.closest('.btn-icon-action--edit') as HTMLButtonElement;
            if (isDelete) {
                const id = Number(isDelete.dataset.id);
                deleteCategoryModal.classList.add('modal-overlay--show');
                confirmDeleteCategoryBtn.addEventListener('click', async () => {
                    await CategoriaService.delete(id);
                    showToast('Categoría eliminada con éxito.', 'success');
                    const updateCategories = await CategoriaService.getAll();
                    renderCategories(updateCategories);
                    deleteCategoryModal.classList.remove('modal-overlay--show');
                });
                closeDeleteModalBtn.addEventListener('click', () => {
                    deleteCategoryModal.classList.remove('modal-overlay--show');
                });
                return;
            }
            if (isEdit) {
                const id = Number(isEdit.dataset.id)
                const categoryToEdit = categories.find((c: ICategory) => c.id === id);
    
                if (categoryToEdit) {
                    categoryNameInput.value = categoryToEdit.name;
                    categoryDescriptionInput.value = categoryToEdit.description;
                    editingCategoryId = id;
                    modalCategory.classList.add('modal-overlay--show');
                }
                return;
            }
    });
});
