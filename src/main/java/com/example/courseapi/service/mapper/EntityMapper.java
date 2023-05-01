package com.example.courseapi.service.mapper;

import java.util.List;
import java.util.Set;

/**
 * Contract for a generic dto to entity mapper.
 @param <RequestDTO> - Request DTO type parameter.
 @param <ResponseDTO> - Response DTO type parameter.
 @param <E> - Entity type parameter.
 */
public interface EntityMapper <RequestDTO, ResponseDTO, E> {

    E fromRequestDto(RequestDTO dto);
    E fromResponseDto(ResponseDTO dto);
    RequestDTO toRequestDto(E entity);
    ResponseDTO toResponseDto(E entity);

    List<E> fromRequestDto(List<RequestDTO> re);
    List<E> fromResponseDto(List<ResponseDTO> dto);
    List<RequestDTO> toRequestDto(List<E> entity);
    List<ResponseDTO> toResponseDto(List<E> entity);

    Set<E> fromRequestDto(Set<RequestDTO> dto);
    Set<E> fromResponseDto(Set<ResponseDTO> dto);
    Set<RequestDTO> toRequestDto(Set<E> entity);
    Set<ResponseDTO> toResponseDto(Set<E> entity);

    ResponseDTO fromRequestToResponse(RequestDTO dto);
    RequestDTO fromResponseToRequest(RequestDTO dto);
    List<ResponseDTO> fromRequestToResponse(List<RequestDTO> dto);
    List<RequestDTO> fromResponseToRequest(List<ResponseDTO> dto);
    Set<ResponseDTO> fromRequestToResponse(Set<RequestDTO> dto);
    Set<RequestDTO> fromResponseToRequest(Set<ResponseDTO> dto);
}