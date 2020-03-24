import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { RegisterRequest } from './registerRequest';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;
  registerRequest: RegisterRequest;
  registrationError: String;

  constructor(
      private authService: AuthService,
      private router: Router
  ) { 
    //   this.registerForm = this.formBuilder.group({
    //     username: '',
    //     email: '',
    //     password: '',
    //     confirmPassword: ''
    //   });

      this.registerForm = new FormGroup({
        username: new FormControl(),
        email: new FormControl(),
        password: new FormControl(),
        confirmPassword: new FormControl()
      });

      this.registerRequest = {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      }

      this.registrationError = null;
  }

  ngOnInit(): void {
  }

  onSubmit() {
      this.registrationError = null;

      this.registerRequest.username = this.registerForm.get('username').value;
      this.registerRequest.email = this.registerForm.get('email').value;
      this.registerRequest.password = this.registerForm.get('password').value;
      this.registerRequest.confirmPassword = this.registerForm.get('confirmPassword').value;

      this.authService.register(this.registerRequest).subscribe(data => {
        this.router.navigateByUrl('/register-success')
      }, error => {
        this.registrationError = error.error.message;
      });
  }
}
