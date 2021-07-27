import * as dayjs from 'dayjs';

export interface IConsumer {
  id?: number;
  guid?: string;
  note?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  recordStatusId?: number;
}

export class Consumer implements IConsumer {
  constructor(
    public id?: number,
    public guid?: string,
    public note?: string | null,
    public createdBy?: string | null,
    public createdDate?: dayjs.Dayjs | null,
    public lastModifiedBy?: string | null,
    public lastModifiedDate?: dayjs.Dayjs | null,
    public recordStatusId?: number
  ) {}
}

export function getConsumerIdentifier(consumer: IConsumer): number | undefined {
  return consumer.id;
}
