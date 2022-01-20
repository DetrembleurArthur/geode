package com.geode.net.queries;

import com.geode.net.annotations.Dto;
import com.geode.net.misc.LowLevelSerializer;

import java.io.Serializable;
import java.util.ArrayList;

@Dto(-1)
public class LowQuery implements Serializable
{
    static
    {
        LowLevelSerializer.registerDto(LowQuery.class);
    }

    private String type;
    private ArgDto argDto;

    public LowQuery()
    {
    }

    public LowQuery(SimpleQuery query)
    {
        type = query.getType();
        argDto = new ArgDto();
        query.getArgs().stream().forEach(serializable -> {
            argDto.setData(serializable);
        });
    }



    @Dto.get(0)
    public String getType()
    {
        return type;
    }

    @Dto.set(0)
    public void setType(String type)
    {
        this.type = type;
    }

    @Dto.get(1)
    public ArgDto getArgDto()
    {
        return argDto;
    }

    @Dto.set(1)
    public void setArgDto(ArgDto argDto)
    {
        this.argDto = argDto;
    }

    public GeodeQuery toGeodeQuery()
    {
        GeodeQuery geodeQuery = new GeodeQuery();
        geodeQuery.setType(getType());
        geodeQuery.setCategory(GeodeQuery.Category.NORMAL);
        ArrayList<Serializable> serializables = new ArrayList<>();
        while(argDto != null)
        {
            serializables.add((Serializable) argDto.getData());
            argDto = argDto.getOther();
        }
        geodeQuery.setArgs(serializables);
        return geodeQuery;
    }
}
