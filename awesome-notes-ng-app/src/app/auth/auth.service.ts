import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { RegisterRequest } from './register/registerRequest';
import { Observable, BehaviorSubject } from 'rxjs';
import { LoginRequest } from './login/loginRequest';
import { JwtAuthResponse } from './jwtAuthResponse';
import { map } from 'rxjs/operators';
import { LocalStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private url = "http://localhost:8080/api/auth/";

  public authenticated: BehaviorSubject<boolean>;

  constructor(
      private httpClient: HttpClient,
      private localStorageService: LocalStorageService
    ) { 
        this.authenticated = new BehaviorSubject<boolean>(
            this.localStorageService.retrieve('username') != null && 
            this.localStorageService.retrieve('authenticationToken') != null
        );
    }

  register(registerRequest: RegisterRequest): Observable<any> {
      return this.httpClient.post(this.url + "signup", registerRequest);
  }

  login(loginRequest: LoginRequest): Observable<boolean> {
      return this.httpClient.post<JwtAuthResponse>(this.url + "login", loginRequest).pipe(map(data => {
        this.localStorageService.store('authenticationToken', data.authenticationToken);
        this.localStorageService.store('username', data.username);
        this.authenticated.next(true);
        return true;
      }));
  }

  logout() {
      this.localStorageService.clear('authenticationToken');
      this.localStorageService.clear('username');
      this.authenticated.next(false);
  }

//   isAuthenticated(): Boolean {
//       return this.localStorageService.retrieve('username') != null && 
//         this.localStorageService.retrieve('authenticationToken') != null;
//   }

  isAuthenticated(): Observable<boolean> {
      return this.authenticated.asObservable();
  }
}
