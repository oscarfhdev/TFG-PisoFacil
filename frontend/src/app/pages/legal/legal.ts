import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-legal',
  standalone: true,
  imports: [CommonModule, MatIconModule, RouterLink],
  templateUrl: './legal.html',
  styleUrl: './legal.scss'
})
export class Legal {}
