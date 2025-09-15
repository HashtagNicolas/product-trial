import { Injectable, signal, computed } from '@angular/core';
import { Product } from 'app/products/data-access/product.model';

export interface CartItem {
  product: Product;
  quantity: number;
}

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private readonly _items = signal<CartItem[]>([]);

  readonly items = this._items.asReadonly();

  // total item
  readonly totalCount = computed(() =>
    this._items().reduce((sum, item) => sum + item.quantity, 0)
  );

  //add product
  add(product: Product) {
    const items = [...this._items()];
    const index = items.findIndex((i) => i.product.id === product.id);

    if (index > -1) {
      items[index] = {
        ...items[index],
        quantity: items[index].quantity + 1,
      };
    } else {
      items.push({ product, quantity: 1 });
    }
    this._items.set(items);
  }

  // delete product
  remove(productId: number) {
    this._items.update((items) =>
      items.filter((i) => i.product.id !== productId)
    );
  }

  //clear cart
  clear() {
    this._items.set([]);
  }
}
