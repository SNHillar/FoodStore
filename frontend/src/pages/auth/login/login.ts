import { loginService } from "../../../services/loginService";
import { redirectAfterLogin } from "../../../utils/auth";
import { saveUser } from "../../../utils/localStorage";

const form = document.getElementById("form") as HTMLFormElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
const inputPassword = document.getElementById("password") as HTMLInputElement;


async function login() {
  try {
    const user = await loginService.login(inputEmail.value, inputPassword.value);
    if (!user) {
      showError("Correo o contraseña incorrectos");
      return;
    }

    user.loggedIn = true;
    saveUser(user);
    redirectAfterLogin(user);
  
  } catch (error) {
    showError("Error al iniciar sesión");
  }
}



form.addEventListener("submit", (e: SubmitEvent) => {
  e.preventDefault();
  login();
});

function showError(message: string) {
  const errorMessage = document.getElementById("error-message") as HTMLParagraphElement;
  errorMessage.textContent = message;
  errorMessage.style.display = "block";
}