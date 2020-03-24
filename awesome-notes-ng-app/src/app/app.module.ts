import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterSuccessComponent } from './auth/register-success/register-success.component';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Ng2Webstorage } from 'ngx-webstorage';
import { HomeComponent } from './home/home.component';
import { AddNoteComponent } from './add-note/add-note.component';
import { HttpClientInterceptor } from './http-client-interceptor';
import { AuthGuard } from './auth.guard';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    RegisterComponent,
    LoginComponent,
    RegisterSuccessComponent,
    HomeComponent,
    AddNoteComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    Ng2Webstorage.forRoot(),
    RouterModule.forRoot([
        { path: '', component: HomeComponent },
        { path: 'register', component: RegisterComponent },
        { path: 'login', component: LoginComponent },
        { path: 'register-success', component: RegisterSuccessComponent },
        { path: 'home', component: HomeComponent },
        { path: 'add-note', component: AddNoteComponent, canActivate: [AuthGuard] },
        { path: 'add-note/:id', component: AddNoteComponent, canActivate: [AuthGuard] }
    ]),
    HttpClientModule
  ],
  providers: [
      {provide: HTTP_INTERCEPTORS, useClass: HttpClientInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
