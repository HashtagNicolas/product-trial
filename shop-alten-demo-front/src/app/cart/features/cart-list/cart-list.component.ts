import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { CartService } from '../../data-access/cart.service';

@Component({
  selector: 'app-cart-list',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './cart-list.component.html',
  styleUrls: ['./cart-list.component.scss'],
})
export class CartListComponent {
  private readonly cartService = inject(CartService);
  readonly items = this.cartService.items;
  readonly totalCount = this.cartService.totalCount;

  remove(productId: number) {
    this.cartService.remove(productId);
  }

  clear() {
    this.cartService.clear();
  }
}