import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Vessel} from "../model/vessel.type";

@Injectable({
  providedIn: 'root'
})
export class VesselService {

  constructor(private http: HttpClient) {
  }

  getUserVessels(): Observable<Vessel[]> {
    return this.http.get<Vessel[]>('barentswatch/monitoring/vessels');
  }
}
