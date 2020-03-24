import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isAuthenticated: Boolean;

  constructor(
      private authService: AuthService,
      private router: Router
  ) { 
      this.isAuthenticated = false;
  }

  ngOnInit(): void {
    this.isAuthenticated = this.authService.isAuthenticated();
  }

  logout(): void {
      this.authService.logout();
      this.router.navigateByUrl("/home");
  }
}
