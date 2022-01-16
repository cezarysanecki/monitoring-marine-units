import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import {MapComponent} from "./map/map.component";
import {LoginPanelComponent} from "./panels/login-panel/login-panel.component";

const routes: Routes = [
  {
    path: "",
    component: MapComponent
  },
  {
    path: "login",
    component: LoginPanelComponent
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
