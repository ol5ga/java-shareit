package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

@AllArgsConstructor
public class ItemMapper {
   public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .request(item.getRequest() != null ? item.getRequest() : null)
                .build();
                // TODO .getId()
   }

   public static Item toItem(ItemDto itemDto){
       return Item.builder()
               .name(itemDto.getName())
               .description(itemDto.getDescription())
               .available(itemDto.isAvailable())
               .request(itemDto.getRequest() != null ? itemDto.getRequest() : null)
               .build();
   }
}
