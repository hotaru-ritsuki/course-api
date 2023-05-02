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

    E fromRequestDto(final RequestDTO dto);
    E fromResponseDto(final ResponseDTO dto);
    RequestDTO toRequestDto(final E entity);
    ResponseDTO toResponseDto(final E entity);

    List<E> fromRequestDto(final List<RequestDTO> re);
    List<E> fromResponseDto(final List<ResponseDTO> dto);
    List<RequestDTO> toRequestDto(final List<E> entity);
    List<ResponseDTO> toResponseDto(final List<E> entity);

    Set<E> fromRequestDto(final Set<RequestDTO> dto);
    Set<E> fromResponseDto(final Set<ResponseDTO> dto);
    Set<RequestDTO> toRequestDto(final Set<E> entity);
    Set<ResponseDTO> toResponseDto(final Set<E> entity);

    ResponseDTO fromRequestToResponse(final RequestDTO dto);
    RequestDTO fromResponseToRequest(final RequestDTO dto);
    List<ResponseDTO> fromRequestToResponse(final List<RequestDTO> dto);
    List<RequestDTO> fromResponseToRequest(final List<ResponseDTO> dto);
    Set<ResponseDTO> fromRequestToResponse(final Set<RequestDTO> dto);
    Set<RequestDTO> fromResponseToRequest(final Set<ResponseDTO> dto);
}