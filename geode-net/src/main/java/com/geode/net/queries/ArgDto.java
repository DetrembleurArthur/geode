package com.geode.net.queries;

import com.geode.net.annotations.Dto;
import com.geode.net.misc.LowLevelSerializer;

@Dto(-2)
    public class ArgDto
    {
        static
        {
            LowLevelSerializer.registerDto(ArgDto.class);
        }

        private Object data;
        private ArgDto other;

        public ArgDto()
        {
        }

        @Dto.get(0)
        public Object getData()
        {
            return data;
        }

        @Dto.set(0)
        public void setData(Object data)
        {
            if(this.data != null)
            {
                if(this.other != null)
                {
                    this.other.setData(data);
                }
                else
                {
                    this.other = new ArgDto();
                    this.other.setData(data);
                }
            }
            else
            {
                this.data = data;
            }
        }

        @Dto.get(1)
        public ArgDto getOther()
        {
            return other;
        }

        @Dto.set(1)
        public void setOther(ArgDto other)
        {
            this.other = other;
        }
    }
