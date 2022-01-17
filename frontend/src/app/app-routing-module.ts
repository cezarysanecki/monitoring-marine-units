import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {LoginPanelComponent} from "./components/panels/login-panel/login-panel.component";
import {MapPanelComponent} from "./components/panels/map-panel/map-panel.component";

const routes: Routes = [
  {
    path: "",
    component: MapPanelComponent
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
