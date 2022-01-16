import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import {MapComponent} from "./map/map.component";
import {LoginComponent} from "./panels/login/login.component";

const routes: Routes = [
  {
    path: "",
    component: MapComponent
  },
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "**",
    redirectTo: "/",
    pathMatch: "full"
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
