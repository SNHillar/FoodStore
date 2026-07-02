import { checkAuhtUser, logout } from "../../../utils/auth";

const buttonLogout = document.getElementById(
  "logout-btn"
) as HTMLButtonElement;
buttonLogout?.addEventListener("click", () => {
  logout();
});


const initPage = () => {
  console.log("inicio de pagina");
  checkAuhtUser(
    "/src/pages/auth/login/login.html",
    "/src/pages/store/home/home.html",
    "admin"
  );
};

initPage();
