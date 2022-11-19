package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.GiftDTO;
import com.beautyline.polimi.entity.GiftEntity;
import com.beautyline.polimi.mapper.GiftMapper;
import com.beautyline.polimi.repository.ConsumerRepository;
import com.beautyline.polimi.repository.GiftRepository;
import com.beautyline.polimi.repository.OrderItemRepository;
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
public class AdminGiftService {

	private final ConsumerRepository consumerRepository;
	private final OrderItemRepository orderItemRepository;
	private final GiftRepository giftRepository;

	public GiftDTO getGift(Long id) {
		return GiftMapper.entityToDto(giftRepository.findById(id).orElse(null));
	}

	public Page<GiftDTO> getGifts(Integer page, Integer size) {
		return GiftMapper.entitiesToDtoPage(giftRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public GiftDTO create(GiftDTO giftDTO) {

		giftDTOControls(giftDTO, true);

		GiftEntity giftEntity = GiftMapper.dtoToEntity(giftDTO);
		giftRepository.save(giftEntity);
		return giftDTO;

	}

	public GiftDTO update(GiftDTO giftDTO) {

		giftDTOControls(giftDTO, false);

		GiftEntity giftEntity = GiftMapper.dtoToEntity(giftDTO);
		giftRepository.save(giftEntity);
		return giftDTO;

	}

	public void delete(Long id) {
		if (id == null || giftRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}

		giftRepository.deleteById(id);
	}

	public void giftDTOControls(GiftDTO giftDTO, boolean create) {

		// id validation
		if (!create && (giftDTO.getId() == null || giftRepository.findById(giftDTO.getId()).isEmpty())) {
			throw new IllegalArgumentException("Invalid ID");
		}

		// consumerID validation
		if (giftDTO.getConsumerId() == null || consumerRepository.findById(giftDTO.getConsumerId()).isEmpty()) {
			throw new IllegalArgumentException("Invalid consumer ID");
		}

		// orderItemId validation
		if (giftDTO.getOrderItemId() == null || orderItemRepository.findById(giftDTO.getOrderItemId()).isEmpty()) {
			throw new IllegalArgumentException("Invalid order item ID");
		}

	}

}
