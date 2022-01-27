import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ToolbarService {

  private filterOnSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public filterOn$: Observable<boolean> = this.filterOnSubject.asObservable();

  changeFilterOn(filterOn: boolean) {
    this.filterOnSubject.next(filterOn);
  }
}
