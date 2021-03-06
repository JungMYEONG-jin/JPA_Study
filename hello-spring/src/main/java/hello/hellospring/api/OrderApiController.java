package hello.hellospring.api;

import hello.hellospring.domain.Address;
import hello.hellospring.domain.Order;
import hello.hellospring.domain.OrderItem;
import hello.hellospring.domain.OrderStatus;
import hello.hellospring.repository.OrderRepository;
import hello.hellospring.repository.OrderSearch;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1()
    {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }


    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2()
    {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3()
    {
        List<Order> orders = orderRepository.findAllwithItem();

        return orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());

    }

    @Getter
    static class OrderDto{

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;


        public OrderDto(Order o) {

            orderId = o.getId();
            name = o.getMember().getName();
            orderDate = o.getOrderDate();
            address = o.getDelivery().getAddress();
            orderItems = o.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem)).collect(Collectors.toList());
        }
    }

    @Getter
    static class OrderItemDto
    {
        private String itemName;
        private int orderPrice;
        private int count;


        public OrderItemDto(OrderItem orderItem)
        {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }


}
