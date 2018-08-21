import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {AuthService} from "./auth.service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class LieuService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getLieuById(codeLieu: string): Observable<any> {
    return this.http
      .get('/api/lieu/' + codeLieu, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getLieuxTeachningCourses(): Observable<any> {
    return this.http
      .get('/api/lieu?with-courses=true', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

}
