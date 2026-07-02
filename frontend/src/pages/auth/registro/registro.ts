import type { IUser } from "../../../types/IUser";
import { navigate } from "../../../utils/navigate";
import { registerService } from "../../../services/registerService";
import { saveUser } from "../../../utils/localStorage";

//const users: IUser[] = JSON.parse(localStorage.getItem("users") || "[]");
const form = document.getElementById("form") as HTMLFormElement;


form?.addEventListener("submit", async (e: SubmitEvent) => {
  e.preventDefault();

  const formData = new FormData(form);
  const firstName = formData.get("name") as string;
  const lastName = formData.get("lastName") as string;
  const phone = formData.get("phone") as string;
  const email = formData.get("email") as string;
  const password = formData.get("password") as string;

  

  try {
    const user = await registerService.register(firstName, lastName, email, password, phone) as IUser;

    //users.push({ email, password, phone, loggedIn: true, role: "client", firstName, lastName });
    //localStorage.setItem("users", JSON.stringify(users));
    form.reset();
    saveUser(user as IUser);

    sessionStorage.setItem("flashMessage", "¡Bienvenido!");
    sessionStorage.setItem("flashType", "success");

    navigate("/src/pages/store/home/home.html");
  } catch (error) {
    console.error("Error al registrar el usuario:", error);
    showError("Error al registrar el usuario");
  }
});

function showError(message: string) {
  const errorMessage = document.getElementById("error-message") as HTMLParagraphElement;
  errorMessage.textContent = message;
  errorMessage.style.display = "block";

  setTimeout(() => {
    errorMessage.style.display = "none";
  }, 3000);
}