package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewtItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto postRequest(Long userId, NewtItemRequestDto newtItemRequestDto);

    List<ItemRequestDto> getRequests(Long userId);

    List<ItemRequestDto> getAllRequests(Long userId);

    ItemRequestDto getRequest(Long userId, Long requestId);

}
