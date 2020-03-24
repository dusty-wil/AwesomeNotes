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
  ) { }

  ngOnInit(): void {
    this.authService.isAuthenticated().subscribe((value) => {
        console.log(value);
        this.isAuthenticated = value;
    });
  }

  logout(): void {
      this.authService.logout();
      this.isAuthenticated = false;
      this.router.navigateByUrl("/home");
  }
}
