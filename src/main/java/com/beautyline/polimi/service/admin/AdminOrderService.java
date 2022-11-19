package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.OrderDTO;
import com.beautyline.polimi.mapper.OrderMapper;
import com.beautyline.polimi.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminOrderService {

	private final OrderRepository orderRepository;

	public OrderDTO getOrder(Long id) {
		return OrderMapper.entityToDto(orderRepository.findById(id).orElse(null));
	}

	public Page<OrderDTO> getOrders(Integer page, Integer size) {
		return OrderMapper.entitiesToDtoPage(orderRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public void delete(Long id) {
		if (id == null || orderRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}

		orderRepository.deleteById(id);
	}

}
