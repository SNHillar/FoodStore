import { TOAST_ICONS } from "./icons";

export function showToast(message: string, type: 'success' | 'error'){

    const toast = document.getElementById("toast") as HTMLDivElement;
    const toastIcon = document.getElementById("toast-icon") as HTMLDivElement;
    const toastMessage = document.getElementById("toast-message") as HTMLParagraphElement;

    if(!toast || !toastIcon || !toastMessage) return;

    toastMessage.textContent = message;
    toastIcon.innerHTML = TOAST_ICONS[type];

    toast.className = "toast";
    toast.classList.add(`toast--${type}`);

    void toast.offsetWidth;


    toast.classList.add("toast--show")

    setTimeout( () => {
        toast.classList.remove("toast--show")
    }, 3000 )
}