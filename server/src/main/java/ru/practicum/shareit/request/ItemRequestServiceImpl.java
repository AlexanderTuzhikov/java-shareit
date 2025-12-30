package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewtItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    @Transactional
    public ItemRequestDto postRequest(Long userId, NewtItemRequestDto newtItemRequestDto) {
        User user = checkUserExists(userId);
        ItemRequest itemRequest = itemRequestMapper.mapToItemRequest(newtItemRequestDto);
        itemRequest.setRequestor(user);
        ItemRequest savedItemRequest = itemRequestRepository.save(itemRequest);

        return itemRequestMapper.mapToItemRequestDto(savedItemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequests(Long userId) {
        checkUserExists(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(userId);

        return itemRequests.stream()
                .map(itemRequestMapper::mapToItemRequestDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId) {
        checkUserExists(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));

        return itemRequests.stream()
                .map(itemRequestMapper::mapToItemRequestDto)
                .peek(itemRequestDto -> itemRequestDto.setItems(new ArrayList<>()))
                .toList();
    }

    @Override
    public ItemRequestDto getRequest(Long userId, Long requestId) {
        checkUserExists(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("itemRequest id=" + requestId + " not found"));

        return itemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User {} not found", userId);
                    return new NotFoundException("User ID=" + userId + " not found");
                });
    }
}
