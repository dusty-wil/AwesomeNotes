import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { LoginRequest } from './loginRequest';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loginRequest: LoginRequest;
  loginError: String;

  constructor(
      private authService: AuthService,
      private router: Router
  ) { 
    this.loginForm = new FormGroup({
      username: new FormControl(),
      password: new FormControl()
    });
    this.loginError = null;
    this.loginRequest = {
      username: '',
      password: ''
    }
  }

  ngOnInit(): void {
  }

  onSubmit() {
      this.loginError = null;

      this.loginRequest.username = this.loginForm.get('username').value;
      this.loginRequest.password = this.loginForm.get('password').value;

      this.authService.login(this.loginRequest).subscribe(data => {
        if (data) {
          this.router.navigateByUrl("/home");
        } else {
          this.loginError = "Invalid username or password.";
        }
      }, error => {
        this.loginError = "Invalid username or password.";
      })
  }
}
