package com.rvtech.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.rvtech.common.PurchaseOrderDto;
import com.rvtech.constant.Constants;
import com.rvtech.entity.PurchaseOrderEntity;
import com.rvtech.repository.PurchaseOrderRepository;
import com.rvtech.util.GenericMapper;
import com.rvtech.util.Utilities;

@Service
public class PurchaseOrderServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

	static ModelMapper modelMapper = new ModelMapper();

	private MultiValueMap<String, String> headers = null;

	private Map<String, Object> responceMap;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	public ResponseEntity<?> savePODeatils(PurchaseOrderDto purchaseOrderDto) {

		PurchaseOrderEntity purchaseOrderEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			purchaseOrderEntity = purchaseOrderRepository
					.save(GenericMapper.mapper.map(purchaseOrderDto, PurchaseOrderEntity.class));
			if (purchaseOrderEntity.getId() != null) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "PO Detail created successfully");
				responceMap.put("Id", purchaseOrderEntity.getId());
				responceMap.put("Status", HttpStatus.OK);
			} else {
				headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
				headers.add(Constants.MESSAGE, "Something went wrong");
				responceMap.put("Id", purchaseOrderEntity.getId());
				responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::saveCarDeatils::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Id", purchaseOrderEntity.getId());
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> search(int pageIndex, int pageSize, String clientName) {
		Pageable page = PageRequest.of(pageIndex, pageSize);
		List<PurchaseOrderDto> dtos = new ArrayList<PurchaseOrderDto>();
		PurchaseOrderEntity purchaseOrderEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			List<PurchaseOrderEntity> purchaseOrderEntityList = purchaseOrderRepository
					.findByClientNameContaining(clientName, page);
			if (purchaseOrderEntityList != null && purchaseOrderEntityList.isEmpty()) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "PO Detail featched successfully");
				for (PurchaseOrderEntity orderEntity : purchaseOrderEntityList) {
					dtos.add(GenericMapper.mapper.map(orderEntity, PurchaseOrderDto.class));
				}
			} else {
				headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
				headers.add(Constants.MESSAGE, "No data available");
				responceMap.put("Status", HttpStatus.NO_CONTENT);
				return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::saveCarDeatils::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Id", purchaseOrderEntity.getId());
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(dtos, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> read(String poId) {

		Optional<PurchaseOrderEntity> purchaseOrderEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			purchaseOrderEntity = purchaseOrderRepository.findByIdAndActive(poId, true);
			if (purchaseOrderEntity != null && purchaseOrderEntity.get().getId() != null) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "PO Detail featched successfully");
			} else {
				headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
				headers.add(Constants.MESSAGE, "No data available");
				responceMap.put("Status", HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::saveCarDeatils::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Id", purchaseOrderEntity.get());
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

	public ResponseEntity<?> delete(String poId) {
		int update;
		Optional<PurchaseOrderEntity> purchaseOrderEntity = null;
		responceMap = new HashMap<String, Object>();
		headers = Utilities.getDefaultHeader();
		try {
			update = purchaseOrderRepository.updateActive(poId, false);
			if (update > 0) {
				headers.add(Constants.STATUS, HttpStatus.OK.toString());
				headers.add(Constants.MESSAGE, "PO deleted successfully");
				responceMap.put("Status", HttpStatus.OK);
			} else {
				headers.add(Constants.STATUS, HttpStatus.NO_CONTENT.toString());
				headers.add(Constants.MESSAGE, "No data available");
				responceMap.put("Status", HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("PurchaseOrderServiceImpl::delete::" + e.getMessage());
			headers.add(Constants.STATUS, HttpStatus.INTERNAL_SERVER_ERROR.toString());
			headers.add(Constants.MESSAGE, "Something went wrong");
			responceMap.put("Id", purchaseOrderEntity.get().getId());
			responceMap.put("Status", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responceMap, headers, HttpStatus.OK);

	}

}
