import { createRouter, createWebHistory } from "vue-router";
import Main from "../views/Main.vue";
import Detail from "../views/Detail.vue";
import Add from "../views/Add.vue";
import Edit from "../views/Edit.vue";
import UserMain from "../views/UserMain.vue";
import UserDetail from "../views/UserDetail.vue";


const history = createWebHistory(import.meta.env.BASE_URL)

const routes = [
  {
    path: "/admin/announcement",
    name: "Main",
    component: Main,
  },
  {
    path: "/admin/announcement/:id",
    name: "Detail",
    component: Detail,
  },
  {
    path: "/admin/announcement/add",
    name: "Add",
    component: Add,
  },
  {
    path: "/admin/announcement/:id",
    name: "Edit",
    component: Edit,
  },
  {
    path: "/announcement",
    name: "UserMain",
    component: UserMain,
  },
  {
    path: "/announcement/:id",
    name: "UserDetail",
    component: UserDetail,
  },
];

const router = createRouter({ history, routes });

export default router;
