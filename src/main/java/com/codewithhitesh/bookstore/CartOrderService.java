package com.codewithhitesh.bookstore;

import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CartOrderService {


    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private CartItemRepository cartItemRepository;

    private UserBookService userBookService;

    public CartOrderService(CartRepository cartRepository, OrderRepository orderRepository,
                            CartItemRepository cartItemRepository, UserBookService userBookService) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.userBookService = userBookService;
    }

    public Cart addToCart(int bookId, int userId,int quantity) {
        User user = userBookService.getUserById(userId);
        Book book = userBookService.getBookById(bookId);

        Cart cart = cartRepository.findByUser(user).orElse(null);

        if (cart == null){
            cart = new Cart();
            cart.setUser(user);
            cart.setCartValue(0);
            cart = cartRepository.save(cart);
        }

        CartItem cartItem = cartItemRepository.findItemByCartAndBook(cart,book).orElse(null);

        /*cartItem.setBook(book);        OLD VERSION of the method  (Problem - makes new row on every
                                                                           quantity++ for same iem
        cartItem.setQuantity(quantity);
        cartItem.setCart(cart);*/

        if (cartItem != null) {
            // 2. If it exists, just update the quantity of the existing row
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            // 3. If it doesn't exist, ONLY THEN create a new row
            cartItem = new CartItem();
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
        }


        // Update cart value
        cart.setCartValue(cart.getCartValue() + (book.getSellingPrice() * quantity));

        cartItemRepository.save(cartItem);
        return cartRepository.save(cart);

    }

    public Order placeOrder(int cartId){
        Cart tempCart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException
                ("User with ID " + cartId + " not found"));

        int tempOrder  = tempCart.getCartValue();
        if (tempOrder <= 0){
            throw new RuntimeException("Cannot place order: Cart is empty!");
        }
        Order order = new Order();
        order.setCart(tempCart);
        order.setUser(tempCart.getUser());
        order.setOrderAmount(tempCart.getCartValue());

        tempCart.setCartValue(0);  // resets cart value to again zero and makes cart empty
        cartRepository.save(tempCart);
        return orderRepository.save(order);

    }
}


/*
(Improved version of addToCart)  OPTION - 01

    public Cart addToCart(int userId, int bookId, int quantity) {
        // 1. Get User and Book
        User user = userBookService.getUserById(userId);
        Book book = userBookService.getBookById(bookId);

        // 2. Get or Create Cart
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart); // Save to get an ID
        }

        // 3. Logic: Check if book is already in the list
        CartItem existingItem = null;
        List<CartItem> items = cart.getCartItems(); // Get the list first for cleaner code

        for (int i = 0; i < items.size(); i++) {
        CartItem currentItem = items.get(i); // Access item by index

        if (currentItem.getBook().getBookId() == bookId) {
        existingItem = currentItem;
        break; // Stop looking once found
    }
        }

        // 4. Update or Create Item
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem(book, cart, quantity);
            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem); // Sync the list
        }

        // 5. Simple Price Calculation
        int total = 0;
        for (CartItem item : cart.getCartItems()) {
            total += (item.getBook().getSellingPrice() * item.getQuantity());
        }
        cart.setCartValue(total);

        return cartRepository.save(cart);
    }

    *****************************OPTION 02********************************
    **********************************************************************

    public Cart addToCart(int bookId, int userId, int quantity) {
    // 1. Get the data using your existing service
    User user = userBookService.getUserById(userId);
    Book book = userBookService.getBookById(bookId);

    // 2. Find the cart or create a new one
    Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
        Cart newCart = new Cart();
        newCart.setUser(user);
        newCart.setCartValue(0);
        return cartRepository.save(newCart); // SAVE THE CART FIRST
    });

    // 3. Create the Item and link it to the SAVED cart
    CartItem cartItem = new CartItem();
    cartItem.setBook(book);
    cartItem.setQuantity(quantity);
    cartItem.setCart(cart);

    // 4. Update calculations
    cart.setCartValue(cart.getCartValue() + (book.getSellingPrice() * quantity));

    // 5. Save the items and the updated cart
    cartItemRepository.save(cartItem);
    return cartRepository.save(cart);
}
}
 */
