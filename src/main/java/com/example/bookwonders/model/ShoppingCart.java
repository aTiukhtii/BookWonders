package com.example.bookwonders.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
//@SQLDelete(sql = "UPDATE shopping_carts SET is_deleted = TRUE WHERE id=?")
//@Where(clause = "is_deleted=false")
@Table(name = "shopping_carts")
@Entity
public class ShoppingCart {
    @Id
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @PrimaryKeyJoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "shoppingCart")
    private Set<CartItem> cartItems;
    //    @Column(nullable = false)
    //    private boolean isDeleted = false;

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setShoppingCart(this);
    }
}
