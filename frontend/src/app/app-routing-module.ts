import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {LoginPanelComponent} from "./components/panels/login-panel/login-panel.component";
import {MapPanelComponent} from "./components/panels/map-panel/map-panel.component";
import {AuthGuard} from "./auth/auth.guard";
import {NoLoggedGuard} from "./auth/no-logged.guard";

const routes: Routes = [
  {
    path: "",
    component: MapPanelComponent
  },
  {
    path: "login",
    component: LoginPanelComponent,
    canActivate: [NoLoggedGuard]
  },
  {
    path: "app/**",
    component: MapPanelComponent,
    canActivate: [AuthGuard]
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
